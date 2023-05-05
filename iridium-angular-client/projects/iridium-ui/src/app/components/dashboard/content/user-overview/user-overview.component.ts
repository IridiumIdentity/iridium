import { Component, Input, } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FrontEndClientSummary } from '../../domain/frontEndClientSummary';

@Component({
  selector: 'create-user-dialog',
  templateUrl: './create-user-dialog.html',
  styleUrls: ['./create-user-dialog.css']
})
export class CreateUserDialog {
  fontStyleControl = new UntypedFormControl('');
  fontStyle?: string;
  createApiFormGroup: UntypedFormGroup;

  // @ts-ignore
  constructor(public dialogRef: MatDialogRef<UserOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
    this.createApiFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
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
  dataSource: FrontEndClientSummary[] = [];
  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) { }


  create() {
    const dialogRef = this.dialog.open(CreateUserDialog, {
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
