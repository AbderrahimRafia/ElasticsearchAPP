import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatPaginator, MatInput } from '@angular/material';


@Component({
  selector: 'app-conference',
  templateUrl: './conference.component.html',
  styleUrls: ['./conference.component.css']
})
export class ConferenceComponent implements OnInit {
  
  listConferences;
  motClé;
  location;
  date;
  personne;
  tweetText;
  constructor(private httpClient: HttpClient) { }

  dataSource: MatTableDataSource<any>;
  searchKey: string;
  displayedColumns: string[] = ['Hashtags','location','date','Personnes','text'];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngOnInit() {
    this.httpClient.get("http://localhost:8080/conferences/all").subscribe(data=>{
      this.listConferences = data;
      this.dataSource = new MatTableDataSource(this.listConferences); 
      this.dataSource.paginator  = this.paginator;
      this.dataSource.filterPredicate = (data, filter) => {
        return this.displayedColumns.some(ele => {
          return ele != 'actions' && data[ele].toLowerCase().indexOf(filter) != -1;
        });
      };
      
    },err=>{
      console.log(err);
    });
    
  }
  

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  

  

}
