import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ApplicationListComponent} from "./application/application-list/application-list.component";
import {ApplicationCreateComponent} from "./application/application-create/application-create.component";
import {ApplicationDetailsComponent} from "./application/application-details/application-details.component";
import {ErrorDetailsComponent} from "./error/error-details/error-details.component";

const routes: Routes = [
  {path: 'application', component: ApplicationListComponent},
  {path: 'application-create', component: ApplicationCreateComponent},
  {path: 'application/:id', component: ApplicationDetailsComponent},
  {path: 'application/:appId/error/:id', component: ErrorDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
