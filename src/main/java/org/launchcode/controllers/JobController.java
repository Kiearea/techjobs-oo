package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.Position;
import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(defaultValue = "0") int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job someJob = jobData.findById(id);
        model.addAttribute("someJob", someJob);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {

            model.addAttribute("name", jobForm);
            model.addAttribute("employer", jobForm);
            model.addAttribute("location", jobForm);
            model.addAttribute("coreCompetencies", jobForm);
            model.addAttribute("positionType", jobForm);

            return "new-job";
        }

        //gets all employers from jobData, Finds the ID associated with each employer and matches it with the employer ID from the jobForm
        Employer emp = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location loc = jobData.getLocations().findById(jobForm.getLocationId());
        PositionType pos = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
        CoreCompetency core = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetenciesId());

        Job someJob = new Job(
            jobForm.getName(),
            emp,
            loc,
            pos,
            core
        );

        model.addAttribute("someJob", someJob);

        jobData.add(someJob);

        return "job-detail";

    }
}
