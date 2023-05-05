import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { FrontEndClientSummary } from '../../domain/frontEndClientSummary';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { CookieService } from '../../../../services/cookie.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
@Component({
  selector: 'create-application-dialog',
  templateUrl: 'create-application-dialog.html',
  styleUrls: ['/create-application-dialog.css']
})
export class CreateApplicationDialog {
  fontStyleControl = new UntypedFormControl('');
  fontStyle?: string;
  createApplicationFormGroup: UntypedFormGroup;
  // @ts-ignore
  constructor(public dialogRef: MatDialogRef<ApplicationOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
    this.createApplicationFormGroup = this._formBuilder.group({
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
  selector: 'app-front-end-client-overview',
  templateUrl: './application-overview.component.html',
  styleUrls: ['./application-overview.component.css']
})
export class ApplicationOverviewComponent implements DynamicContentViewItem {

  @Input() data: any;


  displayedColumns: string[] = ['name', 'clientId', 'type'];
  dataSource: FrontEndClientSummary[] = [];
  createApplicationFormGroup: UntypedFormGroup;


  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) {
    this.createApplicationFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
    });
    this.route.paramMap.subscribe((params: ParamMap) => {
      if (params.get('tenantId')) {

      }
    })
  }

  create() {
    const dialogRef = this.dialog.open(CreateApplicationDialog, {
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
