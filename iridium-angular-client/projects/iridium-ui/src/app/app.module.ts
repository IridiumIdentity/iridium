import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule }  from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { LoginComponent } from './components/login/login.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from '@angular/cdk/layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { RegisterComponent } from './components/register/register.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { CreateTenantPromptDialog, DashboardComponent } from './components/dashboard/dashboard.component';
import { MatTreeModule } from '@angular/material/tree';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { MatExpansionModule } from '@angular/material/expansion';

import { DynamicContentViewService } from './components/dashboard/content/dynamic-content-view.service';
import { DynamicContentViewDirective } from './components/dashboard/content/dynamic-content-view.directive';
import {
  CreateUserDialog,
  UserOverviewComponent
} from './components/dashboard/content/user-overview/user-overview.component';
import {
  CreateRoleDialog,
  RolesOverviewComponent
} from './components/dashboard/content/roles-overview/roles-overview.component';
import {
  ApplicationOverviewComponent,
  CreateApplicationDialog
} from './components/dashboard/content/application-overview/application-overview.component';
import {
  ApiOverviewComponent,
  CreateAPIDialog
} from './components/dashboard/content/api-overview/api-overview.component';
import { DynamicContentViewComponent } from './components/dashboard/content/dynamic-content-view.component';
import { MatTableModule } from '@angular/material/table';
import { SystemOverviewComponent } from './components/dashboard/content/system-overview/system-overview.component';
import { TenantOverviewComponent } from './components/dashboard/content/tenant-overview/tenant-overview.component';
import { MatMenuModule } from '@angular/material/menu';
import { SubscriptionComponent } from './components/dashboard/content/subscription/subscription.component';
import { BillingComponent } from './components/dashboard/content/billing/billing.component';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    SpinnerComponent,
    ConfirmationDialogComponent,
    DashboardComponent,
    ChangePasswordComponent,
    DynamicContentViewDirective,
    DynamicContentViewComponent,
    UserOverviewComponent,
    RolesOverviewComponent,
    ApplicationOverviewComponent,
    ApiOverviewComponent,
    SystemOverviewComponent,
    TenantOverviewComponent,
    CreateTenantPromptDialog,
    SubscriptionComponent,
    BillingComponent,
    CreateApplicationDialog,
    CreateAPIDialog,
    CreateUserDialog,
    CreateRoleDialog
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    ReactiveFormsModule,
    LayoutModule,
    MatSidenavModule,
    MatListModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    MatDialogModule,
    MatTreeModule,
    MatExpansionModule,
    MatTableModule,
    MatMenuModule,
    MatToolbarModule,
    MatButtonToggleModule,
    MatCheckboxModule
  ],
  providers: [DynamicContentViewService],
  bootstrap: [AppComponent]
})
export class AppModule { }
