import { ConfirmationDialogComponent } from './../confirmation-dialog/confirmation-dialog.component';
import { RegisterService } from './../../services/register.service';
import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  notProcessing: Boolean = true;
  registerForm = this.formBuilder.group({
    emailAddress: ''
  });

  constructor(private formBuilder: UntypedFormBuilder, private registerService: RegisterService, public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  register(): void {
    
    console.warn('Your request has been submitted', this.registerForm.get('emailAddress')?.value);
    this.notProcessing = false;
    const emailAddress = this.registerForm.get('emailAddress')?.value;
    this.registerService.register(emailAddress)
    .subscribe(identityResponse => {
      console.log("Successful Request", identityResponse);
      this.dialog.open(ConfirmationDialogComponent, {
        
        data: {title: "Success!", content: "Please check your inbox for at " + emailAddress + " for further registration instructions"},
      });
      
    },
    error => {
      console.log("caught an error!!!!!!", error)
      this.dialog.open(ConfirmationDialogComponent, {
        
        data: {title: "Error!", content: error.error},
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
