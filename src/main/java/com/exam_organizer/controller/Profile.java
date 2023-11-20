package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class Profile {

    private final ExamRepository examRepository;


    public Profile(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @RequestMapping("/profile")
    public String profile(Model model){
        System.out.println("from profile...");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ExamOrganizer) {
                ExamOrganizer examOrganizer = (ExamOrganizer) principal;
                Long organizerId = examOrganizer.getOrganizerId();
                String name = examOrganizer.getFirstName()+" "+examOrganizer.getLastName();
                System.out.println("Organizer Id: " + organizerId);
                System.out.println(name);
                int total=0;
                int cancel=0;
                int active=0;
                int live=0;
                try{
                    List<ExamModel> exam = examRepository.findAllExamsByOrganizerId(organizerId);
                    for(ExamModel i: exam){
//                        System.out.println(i.getExamId());
                        total+=1;
                        String s = String.valueOf(i.getStatus());

                        if("ACTIVE" == s){
                            active+=1;
                        }else if("CANCELLED"==s){
                            cancel+=1;
                        }else{
                            live+=1;
                        }

                    }
                    System.out.println(active);
                    System.out.println(cancel);
                    System.out.println(live);
                    System.out.println(total);
                }catch (Exception ex){
                    System.out.println("error while total count exam: "+ex);
                }


                model.addAttribute("name",name);
                model.addAttribute("id",organizerId);
                model.addAttribute("exam",total);
                model.addAttribute("cancel",cancel);
                model.addAttribute("active",active);
                model.addAttribute("live",live);


            } else {
                System.out.println("Principal is not of type ExamOrganizer");
            }
        } else {
            System.out.println("User not authenticated");
        }
        return "profile";
    }
}
