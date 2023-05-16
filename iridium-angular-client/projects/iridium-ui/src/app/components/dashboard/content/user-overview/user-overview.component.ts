import { Component, Input, } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationSummary } from '../../domain/application-summary';

@Component({
  selector: 'add-user-dialog',
  templateUrl: './add-user-dialog.html',
  styleUrls: ['./add-user-dialog.css']
})
export class AddUserDialog {
  createUserFormGroup: UntypedFormGroup;

  constructor(public dialogRef: MatDialogRef<UserOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
    this.createUserFormGroup = this._formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  create() {
    console.log('yes')
  }
}

@Component({
  selector: 'app-user-overview',
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.css']
})
export class UserOverviewComponent implements DynamicContentViewItem {
  @Input() data: any;
  displayedColumns: string[] = ['name', 'clientId', 'type'];
  dataSource: ApplicationSummary[] = [];
  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) { }


  create() {
    const dialogRef = this.dialog.open(AddUserDialog, {
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
  onRowClick(index: number) {
    console.log('clicked on row: ', index)
  }
}
