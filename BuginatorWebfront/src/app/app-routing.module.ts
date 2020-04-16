import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ApplicationListComponent} from "./application/application-list/application-list.component";
import {ApplicationCreateComponent} from "./application/application-create/application-create.component";

const routes: Routes = [
  {path: 'application', component: ApplicationListComponent},
  {path: 'application-create', component: ApplicationCreateComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
