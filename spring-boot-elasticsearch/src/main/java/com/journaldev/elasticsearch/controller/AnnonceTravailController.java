package com.journaldev.elasticsearch.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journaldev.elasticsearch.bean.AnnonceTravail;
import com.journaldev.elasticsearch.bean.Conference;
import com.journaldev.elasticsearch.dao.AnnonceTravailDao;
import com.journaldev.elasticsearch.dao.ConferenceDao;

@RestController
@RequestMapping("/offres")
public class AnnonceTravailController {
	
	@Autowired
	private AnnonceTravailDao annonceTravailDao;

    public AnnonceTravailController(AnnonceTravailDao annonceTravailDao) {
        this.annonceTravailDao = annonceTravailDao;
    }

    @PostMapping
    public AnnonceTravail insertBook(@RequestBody AnnonceTravail annonceTravail) throws Exception{
        return annonceTravailDao.insertAnnonceTravail(annonceTravail);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getAnnonceTravailById(@PathVariable String id){
    	return annonceTravailDao.getAnnonceTravailById(id);
    }
    
    @GetMapping("/all")
    public List<Map<String, Object>> getAll(){
    	return annonceTravailDao.getAllAnnoneTravails();
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateBookById(@RequestBody AnnonceTravail annonceTravail, @PathVariable String id){
        return annonceTravailDao.updateAnnonceTravailById(id, annonceTravail);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable String id){
    	annonceTravailDao.deleteAnnonceTravailById(id);
    }
}
