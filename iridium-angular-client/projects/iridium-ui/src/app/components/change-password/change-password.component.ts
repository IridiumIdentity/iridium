import { ActivatedRoute } from '@angular/router';
import { PasswordResetRequest } from './../../services/domain/password-reset-request';
import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PasswordService } from 'src/app/services/password.service';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  
  notProcessing: Boolean = true;
  registerForm = this.formBuilder.group({
    password: '',
    passwordConfirmation: '',
  });

  token: string = '';

  constructor(private formBuilder: UntypedFormBuilder, private passwordService: PasswordService, public dialog: MatDialog, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    var token = this.activatedRoute.snapshot.queryParamMap.get('token');
    if (token) {
      this.token = token;
    }
  }

  resetPassword(): void {
    
    console.warn('Your request has been submitted', this.registerForm.get('emailAddress')?.value);
    this.notProcessing = false;
    const emailAddress = this.registerForm.get('password')?.value;

    var request: PasswordResetRequest = {
      newPassword: this.registerForm.get("password")?.value,
      token: this.token
      
    }

    this.passwordService.resetPassword(request)
    .subscribe(identityResponse => {
      console.log("Successful Request", identityResponse);
      this.dialog.open(ConfirmationDialogComponent, {
        
        data: {title: "Success!", content: "Password successfully changed"},
      });
      
    },
    error => {
      console.log("caught an error!!!!!!", error)
      this.dialog.open(ConfirmationDialogComponent, {
        
        data: {title: "Error changing password!", content: error.error},
      });
      this.notProcessing = true;
    },
    () => {
      console.log("This runs by default for each successful call")
      this.notProcessing = true;
    })
    this.registerForm.reset();
  }

}
