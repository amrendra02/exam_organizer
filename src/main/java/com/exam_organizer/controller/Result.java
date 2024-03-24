package com.exam_organizer.controller;

import com.exam_organizer.dto.ExamResultDto;
import com.exam_organizer.model.ExamResultModel;
import com.exam_organizer.service.ResultService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.geom.RectangularShape;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class Result {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ModelMapper modelMapper;

    private Logger log = LoggerFactory.getLogger(Result.class);
    @RequestMapping("/result")
    public String result(){
       log.info("from result...");
        return "result";
    }

    @GetMapping("/exam/{examId}/result/{page}")
    public ResponseEntity<?> getAllResult(@PathVariable Long examId,@PathVariable int page)
    {
        log.info("Processing to get all result of exam ID: {}",examId);
        List<ExamResultModel> resultData;
        try{
            Page<ExamResultModel> resultList = resultService.candidateList(examId,page);
            if(resultList.getContent()==null){
                return new ResponseEntity<>(Map.of( "Data","not found","one","two"), HttpStatus.OK);
            }
            resultData = resultList.getContent();
            List<ExamResultDto> res;
            res = resultData.stream().map((x) -> this.modelMapper.map(x, ExamResultDto.class)).collect(Collectors.toList());
            for(ExamResultDto i: res){
                log.info("{}",i);
            }

            return  new ResponseEntity<>(res,HttpStatus.OK);
        }catch (Exception ex){
            log.info("{}",ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
