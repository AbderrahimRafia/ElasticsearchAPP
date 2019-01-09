import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatPaginator, MatInput } from '@angular/material';

@Component({
  selector: 'app-offre',
  templateUrl: './offre.component.html',
  styleUrls: ['./offre.component.css']
})
export class OffreComponent implements OnInit {
  listOffres;
  poste;
  date_publication;
  location;
  text;
  constructor(private httpClient: HttpClient) { }

  dataSource: MatTableDataSource<any>;
  searchKey: string;
  displayedColumns: string[] = ['poste','date_publication','location','text'];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngOnInit() {
    this.httpClient.get("http://localhost:8080/offres/all").subscribe(data=>{
      this.listOffres = data;
      this.dataSource = new MatTableDataSource(this.listOffres); 
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
