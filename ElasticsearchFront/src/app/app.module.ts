import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { ConferenceComponent } from './conference/conference.component';

import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatTableModule, MatIconModule, MatFormFieldModule, MatFormFieldControl, MatInputModule, MatToolbarModule, MatGridListModule, MatButtonModule, MatMenuModule, MatTabsModule } from '@angular/material';
import { MatPaginatorModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { OffreComponent } from './offre/offre.component';



@NgModule({
  declarations: [
    AppComponent,
    ConferenceComponent,
    OffreComponent
  ],
  imports: [
    BrowserModule,
    FormsModule ,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatGridListModule,
    MatButtonModule,
    MatMenuModule,
    MatTabsModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
