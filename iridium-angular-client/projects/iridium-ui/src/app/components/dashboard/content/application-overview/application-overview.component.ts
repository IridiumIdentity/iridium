import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { ApplicationSummary } from '../../domain/application-summary';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { CookieService } from '../../../../services/cookie.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ApplicationTypeService } from '../../../../services/application-type.service';
import { ApplicationTypeSummary } from '../../domain/application-type-summary';
import { ApplicationService } from '../../../../services/application.service';
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
  constructor(public dialogRef: MatDialogRef<ApplicationOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private applicationService: ApplicationService) {
    this.createApplicationFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
      applicationTypeId: ['', Validators.required]
    });
  }


  create() {
    console.log('form data', this.createApplicationFormGroup.controls)
    this.applicationService.create(this.createApplicationFormGroup, this.data.tenantId)
      .subscribe((response ) => {
        console.log('create application was good.')
      })
  }
}
@Component({
  selector: 'app-front-end-client-overview',
  templateUrl: './application-overview.component.html',
  styleUrls: ['./application-overview.component.css']
})
export class ApplicationOverviewComponent implements DynamicContentViewItem, OnInit {

  @Input() data: any;


  displayedColumns: string[] = ['name', 'clientId', 'type'];
  dataSource: ApplicationSummary[] = [];
  createApplicationFormGroup: UntypedFormGroup;
  applicationTypes: ApplicationTypeSummary[] = []


  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog, private applicationTypeService: ApplicationTypeService, private applicationService: ApplicationService) {
    this.createApplicationFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
    });
   this.applicationTypeService.get()
     .subscribe(applicationTypes => {
       this.applicationTypes = applicationTypes
     })


  }

  create() {
    const dialogRef = this.dialog.open(CreateApplicationDialog, {
      data: {applicationTypes: this.applicationTypes, tenantId: this.data.tenantId},
    });

    dialogRef.afterClosed().subscribe(result => {

    });
  }
  onRowClick(index: number) {
    console.log('clicked on row: ', index)
  }

  ngOnInit(): void {
    console.log('ng on init', this.data)
    this.applicationService.getPage(this.data.tenantId, 100)
      .subscribe(applicationSummaries => {
        for (let i = 0; applicationSummaries.data.length; i++) {
          console.log('adding row')
          const newRow = {name: applicationSummaries.data[i].name, clientId: applicationSummaries.data[i].clientId, type: applicationSummaries.data[i].id};
          this.dataSource = [...this.dataSource, newRow]
        }

      })
  }
}
