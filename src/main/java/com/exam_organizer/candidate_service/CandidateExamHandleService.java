package com.exam_organizer.candidate_service;

import com.exam_organizer.candidate_Repository.CandidateExamRegisteredRepo;
import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.constant.ApiResponse;
import com.exam_organizer.controller.Candidate;
import com.exam_organizer.dto.AnswerDTO;
import com.exam_organizer.dto.OptionDto;
import com.exam_organizer.dto.QuestionDto;
import com.exam_organizer.dto.RequestOne;
import com.exam_organizer.exception.ResourceNotFoundException;
import com.exam_organizer.model.*;
import com.exam_organizer.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateExamHandleService {

    private Logger log = LoggerFactory.getLogger(CandidateExamHandleService.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateExamRegisteredRepo candidateExamRegisteredRepo;

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CandidateAnswerRepository candidateAnswerRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamResultRepository examResultRepository;


    @Autowired
    private ModelMapper modelMapper;

    public ApiResponse checkRegistration(CandidateModel candidateModel, Long examId) {
        log.info("Checking the exam registration and status");
        try {
            Optional<ExamModel> examModel = examRepository.findById(examId);
            if (!examModel.isPresent()) {
                throw new ResourceNotFoundException("Exam not found", "Exam Id:", examId);
            }

            CandidateExamRegisteredModel register = candidateExamRegisteredRepo.findByCandidateModelAndExamModel(candidateModel, examModel.get());
            if (register == null) {
                throw new ResourceNotFoundException("Credential data not found", "Candidate not registered", examId);
            }

            log.info("{}", register.getStatus());

            if ("Done".equals(register.getStatus())) {
                return new ApiResponse("Candidate already attended the exam", false);
            } else {
                return new ApiResponse("Successfully found the user registered exam", true);
            }
        } catch (Exception ex) {
            return new ApiResponse(ex.getMessage(),false);
        }
    }

    public ApiResponse examParticipate(RequestOne req) {
        log.info("processing the steps.");
        try {
            ApiResponse res = null;
            CandidateModel can = candidateRepository.findByUsername(req.getUsername());
            log.info("step 1: {} === {}", can.getUsername(), req.getUsername());
            if (can.getUsername().equals(req.getUsername())) {
                Optional<ExamModel> exam = examRepository.findById(req.getExamId());
                log.info("step 2");
                if (exam.get() != null) {
                    CandidateExamRegisteredModel data = new CandidateExamRegisteredModel();
                    data.setExamModel(exam.get());
                    data.setCandidateModel(can);
                    CandidateExamRegisteredModel check = candidateExamRegisteredRepo.findByCandidateModelAndExamModel(can,exam.get());
                    if(check==null){
                        CandidateExamRegisteredModel res2 = candidateExamRegisteredRepo.save(data);
                        log.info("step 3");
                        res = new ApiResponse("Successfully addded id: " + res2.getId(), true);
                        return res;
                    }else{
                        res = new ApiResponse("Candidate already registered",false);
                        return res;
                    }

                } else {
                    log.info("failed step 2");
                    res = new ApiResponse("Exam not found", false);
                    return res;
                }
            }
            log.info("failed step 1");
            res = new ApiResponse("user data not correct", false);
            return res;
        } catch (Exception ex) {
            log.info("Exception: {}", ex);
            ApiResponse res = new ApiResponse(ex.getMessage(), false);
            return res;
        }
    }

    public Map<QuestionDto, List<OptionDto>> questionList(Long examId) {
        log.info("question list processing");
        try {
            Map<QuestionDto, List<OptionDto>> res = new HashMap<>();
            List<QuestionModel> questionList = questionRepository.findAllByExamModelExamId(examId);
            log.info("step 1");
            if (questionList.isEmpty()) {
                throw new ResourceNotFoundException("Not found question", "Exam Id:", examId);
            }
            log.info("step 2: {}", questionList.size());
            for (int i = 0; i < questionList.size(); i++) {
                List<OptionDto> optionList = questionList.get(i).getOptionModels().stream().map(x -> modelMapper.map(x, OptionDto.class)).collect(Collectors.toList());
                QuestionDto question = modelMapper.map(questionList.get(i), QuestionDto.class);
                question.setCorrectOption("");
                res.put(question, optionList);
            }
            log.info("step 3");
            return res;
        } catch (Exception ex) {

            log.info("exception: {}", ex.getMessage());
            return null;
        }
    }


    public ApiResponse answerSubmit(AnswerDTO req, Long examId) {
        log.info("Processing the answer submission");
        ApiResponse res = null;
        try {
            CandidateModel candidate = candidateRepository.findByUsername(req.getUsername());
            log.info("step 1:");
            Long canId = candidate.getCandidateId();
            Optional<ExamModel> exam = examRepository.findById(examId);
            log.info("step 2:");
            if (candidate == null) {

                throw new ResourceNotFoundException("Candidate not found", "username" + req.getUsername(), 0L);
            } else if (exam.get() == null) {
                throw new ResourceNotFoundException("Exam not found", "examId", examId);
            } else {
                log.info("step 3:");
                List<CandidateAnswer> answers = new ArrayList<>();




//                ResultModel result = new ResultModel();
                ExamResultModel resultModel = new ExamResultModel();

//                result.setCandidateModel(candidate);
//                result.setExamModel(exam.get());

                int correct=0;
                int incorrect=0;

                // Iterate over the options map
                for (Map.Entry<String, AnswerDTO.OptionDetails> entry : req.getOptions().entrySet()) {
                    log.info("step 4:");
                    String key = entry.getKey();
                    AnswerDTO.OptionDetails optionDetail = entry.getValue();
                    QuestionModel question = questionRepository.findByQuestionId(optionDetail.getQuestionId());

                    if (question == null) {
                        throw new ResourceNotFoundException("Question npt found", "Question Id", optionDetail.getQuestionId());
                    }
                    Optional<OptionModel> option = optionRepository.findById(optionDetail.getOptionId());

                    if (option.get() == null) {
                        throw new ResourceNotFoundException("Selected option not found", "Option Id", optionDetail.getOptionId());
                    }
                    CandidateAnswer answer = new CandidateAnswer();
                    answer.setExam(exam.get());
                    answer.setCandidate(candidate);
                    answer.setQuestion(question);
                    answer.setOption(option.get());
                    if (optionDetail.getOptionNumber() == question.getCorrectOption()) {
                        answer.setStatus("C");
                        correct+=1;
                    }else{
                        answer.setStatus("W");
                        incorrect+=1;
                    }
                    answers.add(answer);
                    log.info("step 5:");
                }
                candidateAnswerRepository.saveAll(answers);

                int total = incorrect+correct;
//                result.setRatio(correct+" / "+total);
//                Type casting miss matched
//                result.setExamDate(exam.get().getExamDate());
                int totalMark=exam.get().getTotalMarks();
                int totalQuestion = incorrect+correct;
                int  perQuestion = totalMark/totalQuestion;
//                result.setMarksObtained(perQuestion*correct);

                resultModel.setExamId(exam.get().getExamId());
                resultModel.setCandidateModel(candidate);
                resultModel.setCorrect(correct);
                resultModel.setWrong(incorrect);
                if(correct>incorrect) {
                    resultModel.setStatus("PASS");
                }else{
                    resultModel.setStatus("Fail");
                }
                log.info("{}",resultModel.getExamId());
                examResultRepository.save(resultModel);
//                resultRepository.save(result);

                log.info("step 6:");
                res = new ApiResponse("Successfully submitted", true);
                log.info("Successfully saved.");
                CandidateExamRegisteredModel registration = candidateExamRegisteredRepo.findByCandidateModelAndExamModel(candidate, exam.get());
                registration.setStatus("Done");
                candidateExamRegisteredRepo.save(registration);
                return res;
            }
        } catch (Exception ex) {
            log.info("Failed: {}", ex.getMessage());
            res = new ApiResponse("" + ex.getMessage(), false);
            return res;
        }
    }
}
