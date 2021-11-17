package cn.jcj.controller;

import cn.jcj.pojo.ParkingRecord;
import cn.jcj.pojo.Region;
import cn.jcj.service.ParkingRecordService;
import cn.jcj.utils.Msg;
import cn.jcj.utils.ValidatedTool;
import cn.jcj.vo.ParkingRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2021/7/2 16:08   @author 纪村杰  @version 1.00
 * Description
 */
@RestController
@CrossOrigin
public class CController {

    @Autowired
    ParkingRecordService parkingRecordService;

    @GetMapping("/sel")
    public List<ParkingRecordVo> sel(Integer id){
        System.out.println("id = " + id);
        return parkingRecordService.sel(id);
    }

    @GetMapping("/selType")
    public List<Region> selType(){
        return parkingRecordService.selType();
    }

    @PutMapping("/upd/{id}")
    public Msg upd(@PathVariable Integer id){
        parkingRecordService.upd(id);
        return Msg.success();
    }

    @PostMapping("/add")
    public Msg add(@RequestBody @Validated ParkingRecord parkingRecord, BindingResult bindingResult){
        HashMap<String, String> map=ValidatedTool.validatedPattern(bindingResult);

        if(map==null){

            parkingRecordService.add(parkingRecord);
            return Msg.success();

        }
return Msg.fail().add("erro",map);

    }





}
