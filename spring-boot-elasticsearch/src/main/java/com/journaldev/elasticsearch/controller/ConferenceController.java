package com.journaldev.elasticsearch.controller;

import com.journaldev.elasticsearch.bean.Conference;
import com.journaldev.elasticsearch.dao.ConferenceDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
	
	@Autowired
    private ConferenceDao conferenceDao;
	
	 public ConferenceController(ConferenceDao conferenceDao) {
	        this.conferenceDao = conferenceDao;
	    }
	
	 @GetMapping("/all")
	public List<Map<String, Object>> getAll(){
	    	return conferenceDao.getAllConferences();
	    }

   

    @PostMapping
    public Conference insertBook(@RequestBody Conference conference) throws Exception{
        return conferenceDao.insertConference(conference);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getConferenceById(@PathVariable String id){
    	return conferenceDao.getConferenceById(id);
    }
    
   

    @PutMapping("/{id}")
    public Map<String, Object> updateBookById(@RequestBody Conference conference, @PathVariable String id){
        return conferenceDao.updateConferenceById(id, conference);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable String id){
    	conferenceDao.deleteConferenceById(id);
    }
}
