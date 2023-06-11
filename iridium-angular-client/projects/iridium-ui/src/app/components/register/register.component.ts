import { ConfirmationDialogComponent } from './../confirmation-dialog/confirmation-dialog.component';
import { RegisterService } from './../../services/register.service';
import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { NgxIridiumClientService } from '../../../../../ngx-iridium-client/src/lib/ngx-iridium-client.service';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {



  constructor(private iridiumClient: NgxIridiumClientService, public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  public signIn() {
    this.iridiumClient.authenticateWithExternalRedirect();
  }
  public register() {
    this.iridiumClient.authenticateWithExternalRedirect();
  }
}
