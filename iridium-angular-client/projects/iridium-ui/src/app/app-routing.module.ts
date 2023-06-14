
import { RegisterComponent } from './components/register/register.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NgxIridiumCallbackComponent } from 'ngx-iridium-client';



const routes: Routes = [
  { path: '', component: RegisterComponent},
  { path: 'dashboard', component: DashboardComponent},
  { path: 'callback', component: NgxIridiumCallbackComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
