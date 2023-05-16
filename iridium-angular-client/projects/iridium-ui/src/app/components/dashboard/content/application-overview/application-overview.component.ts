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
  selector: 'update-application-dialog',
  templateUrl: 'update-application-dialog.html',
  styleUrls: ['/update-application-dialog.css']
})
export class UpdateApplicationDialog {
  updateApplicationFormGroup: UntypedFormGroup;
  constructor(public dialogRef: MatDialogRef<ApplicationOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.updateApplicationFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
      applicationTypeId: ['', Validators.required]
    });
  }


  update() {

    this.dialogRef.close({formGroup: this.updateApplicationFormGroup })
  }
}
@Component({
  selector: 'create-application-dialog',
  templateUrl: 'create-application-dialog.html',
  styleUrls: ['/create-application-dialog.css']
})
export class CreateApplicationDialog {
  createApplicationFormGroup: UntypedFormGroup;
  constructor(public dialogRef: MatDialogRef<ApplicationOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.createApplicationFormGroup = this._formBuilder.group({
      applicationName: ['', Validators.required],
      homepageURL: ['', Validators.required],
      description: [''],
      authorizationCallbackURL: ['', Validators.required],
      applicationTypeId: ['', Validators.required]
    });
  }


  create() {

    this.dialogRef.close({formGroup: this.createApplicationFormGroup })
  }
}

type ApplicationTypeSummaryMapType = {
  [id: string]: ApplicationTypeSummary;
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
  applicationTypes: ApplicationTypeSummary[] = []
  applicationTypeMap: ApplicationTypeSummaryMapType = {};


  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog, private applicationTypeService: ApplicationTypeService, private applicationService: ApplicationService) {
  }

  create() {
    const dialogRef = this.dialog.open(CreateApplicationDialog, {
      data: {applicationTypes: this.applicationTypes, tenantId: this.data.tenantId},
    });

    dialogRef.afterClosed().subscribe(result => {
      this.dataSource = [];
      this.applicationService.create(result.formGroup, this.data.tenantId)
        .subscribe((response ) => {
          this.refreshDataSource();
        })

    });
  }
  onRowClick(index: number) {
    console.log(this.dataSource[index])
    const dialogRef = this.dialog.open(UpdateApplicationDialog, {
      data: {applicationTypes: this.applicationTypes, tenantId: this.data.tenantId, applicationId: this.dataSource[index].id},
    });

    dialogRef.afterClosed().subscribe(result => {
      // update application
      //this.dataSource = [];
      // this.applicationService.create(result.formGroup, this.data.tenantId)
      //   .subscribe((response ) => {
      //     this.refreshDataSource();
      //   })

    });
  }

  ngOnInit(): void {
    this.applicationTypeService.get()
      .subscribe(applicationTypes => {
        this.applicationTypes = applicationTypes

        for (let i = 0; i < applicationTypes.length; i ++) {
          this.applicationTypeMap[applicationTypes[i].id] = applicationTypes[i]
        }
        this.refreshDataSource();
      })

  }

  private refreshDataSource() {
    this.applicationService.getPage(this.data.tenantId, 100)
      .subscribe(applicationSummaries => {
        for (let i = 0; i < applicationSummaries.data.length; i++) {
          let summary = applicationSummaries.data[i];
          const newRow = {
            id: summary.id,
            name: summary.name,
            clientId: summary.clientId,
            type: this.applicationTypeMap[summary.applicationTypeId].name
          };
          this.dataSource = [...this.dataSource, newRow]
        }

      })
  }
}
