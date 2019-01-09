import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as Material from "@angular/material"; 
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    Material.MatTableModule,
    Material.MatPaginatorModule,
    Material.MatIconModule,
    Material.MatFormFieldModule,
    Material.MatToolbarModule,
    Material.MatGridListModule,
    Material.MatButtonModule,
    Material.MatMenuModule,
    Material.MatTabsModule
    
  ],
  exports: [
    Material.MatTableModule,
    Material.MatPaginatorModule,
    Material.MatIconModule,
    Material.MatFormFieldModule,
    Material.MatToolbarModule,
    Material.MatGridListModule,
    Material.MatButtonModule,
    Material.MatMenuModule,
    Material.MatTabsModule


  ],
})
export class MaterialModule { }
