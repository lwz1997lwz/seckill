package org.seckill.controller;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    /**
     * @description:
     * @param: [model]
     * @return: java.lang.String
     */
    @RequestMapping(value = "/seckill/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getAllSeckill();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/seckill/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/list";
        }
        model.addAttribute("seckill", seckill);

        return "detail";
    }

    /**
     * @description: 获取秒杀地址
     * @param: [seckillId]
     * @return: org.seckill.dto.SeckillResult<org.seckill.dto.Exposer>
     */
    @RequestMapping(value = "/seckill/{seckillId}/exposer", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    //需要将ajax请求的结果封装成json数据返回
    public SeckillResult<Exposer> exposeURL(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;

    }

/** 
 * @description: 执行秒杀 
 * @param: [seckillId, md5, phone] 
 * @return: org.seckill.dto.SeckillResult<org.seckill.dto.SeckillExecution> 
 */ 
    @RequestMapping(value = "/seckill/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> excuteSekill(@PathVariable("seckillId") Long seckillId,
                                                        @PathVariable("md5") String md5, @CookieValue(value = "userPhone", required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;

        try {
            SeckillExecution execution = seckillService.excuteSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }

    }

    //获取系统时间
    @RequestMapping(value = "/seckill/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }
}
