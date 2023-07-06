import { Component, Inject, Input, OnInit } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { ExternalProviderResponse } from '../../domain/external-provider-response';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ExternalProviderTemplateService } from '../../../../services/external-provider-template.service';
import {
  ExternalProviderTemplateSummaryResponse
} from '../../../../services/domain/external-provider-template-summary-response';
import { ExternalIdentityProviderService } from '../../../../services/external-identity-provider.service';
import {
  ApplicationOverviewComponent,
  UpdateApplicationDialog
} from '../application-overview/application-overview.component';

@Component({
  selector: 'update-external-provider-dialog',
  templateUrl: 'update-external-provider-dialog.html',
  styleUrls: ['update-external-provider-dialog.css']
})
export class UpdateExternalProviderDialog {
  updateApplicationFormGroup: UntypedFormGroup;
  constructor(public dialogRef: MatDialogRef<LoginBoxOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.updateApplicationFormGroup = this._formBuilder.group({
      applicationName: [this.data.application.name, Validators.required],
      clientId: [{value:  this.data.application.clientId, disabled: true}],
      clientSecret: [this.data.application.homepageURL, Validators.required],
      description: [this.data.application.description],
      authorizationCallbackURL: [this.data.application.callbackURL, Validators.required],
      applicationTypeId: [this.data.application.applicationTypeId, Validators.required],
      privacyPolicyURL: [this.data.application.privacyPolicyUrl, ],
      iconURL: [this.data.application.iconURL ]
    });
  }


  update() {

    this.dialogRef.close({formGroup: this.updateApplicationFormGroup })
  }
}
@Component({
  selector: 'add-external-provider-dialog',
  templateUrl: 'add-external-provider-dialog.html',
  styleUrls: ['add-external-provider-dialog.css']
})
export class AddExternalProviderDialog {
  addProviderDialogFormGroup: UntypedFormGroup;
  externalProviderSummariesCopy: ExternalProviderTemplateSummaryResponse[] = [];

  constructor(public dialogRef: MatDialogRef<LoginBoxOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.addProviderDialogFormGroup = this._formBuilder.group({
      externalProviderTemplateId: ['', Validators.required],
      clientId: ['', Validators.required],
      clientSecret: ['', Validators.required],
    });
    let found = false;
    for (let i in data.externalProviderTemplates) {
      for (let j in data.dataSource) {
        if (data.externalProviderTemplates[i].name === this.data.dataSource.name) {
          found = true;
        }
        if (!found) {
          this.externalProviderSummariesCopy.push(data.externalProviderTemplates[i])
        }
      }

    }
  }



  create() {

    this.dialogRef.close({formGroup: this.addProviderDialogFormGroup })
  }
}

type ExternalProviderTemplateSummaryMapType = {
  [id: string]: ExternalProviderTemplateSummaryResponse;
}
@Component({
  selector: 'app-login-box-overview',
  templateUrl: './login-box-overview.component.html',
  styleUrls: ['./login-box-overview.component.css']
})
export class LoginBoxOverviewComponent implements DynamicContentViewItem, OnInit {
  @Input() data: any;
  displayedColumns: string[] = ['id', 'providerName'];
  dataSource: ExternalProviderResponse[] = [];
  externalProviderTemplateSummaries: ExternalProviderTemplateSummaryResponse[] = [];
  externalTemplateMapType: ExternalProviderTemplateSummaryMapType = {};

  constructor(private externalProviderTemplateService: ExternalProviderTemplateService, private dialog: MatDialog, private externalProviderService: ExternalIdentityProviderService) {
  }

  onRowClick(index: number) {
    console.log('clicked on row: ', index)
    this.externalProviderService.get(this.data.tenantId, this.dataSource[index].id)
      .subscribe(externalProviderResponse => {
        const dialogRef = this.dialog.open(UpdateApplicationDialog, {
          data: {externalProviderTemplates: this.externalProviderTemplateSummaries, tenantId: this.data.tenantId, externalProvider: externalProviderResponse},
        });
        dialogRef.afterClosed().subscribe(updateResult => {

          this.externalProviderService.update(updateResult.formGroup, this.data.tenantId, this.dataSource[index].id)
            .subscribe(result => {
              console.log('update result is: ', result)
              this.refreshDataSource();
            })

        });
      })
  }


  addProvider() {
    const dialogRef = this.dialog.open(AddExternalProviderDialog, {
      data: {externalProviderTemplates: this.externalProviderTemplateSummaries, tenantId: this.data.tenantId, currentProviders: this.dataSource},
    });

    dialogRef.afterClosed().subscribe(result => {

     if (result) {
       this.externalProviderService.create(result.formGroup, this.data.tenantId)
         .subscribe(result => {
           this.refreshDataSource();
         })
     }
    });
  }

  updateLogo() {

  }

  private refreshDataSource() {
    this.dataSource = [];
    this.externalProviderService.getAll(this.data.tenantId)
      .subscribe(applicationSummaries => {
        for (let i = 0; i < applicationSummaries.length; i++) {
          let summary = applicationSummaries[i];
          const newRow = {
            id: summary.id,
            name: summary.name,
            iconPath: summary.iconPath,
          };
          this.dataSource = [...this.dataSource, newRow]
        }

      })
  }

  ngOnInit(): void {
    this.externalProviderTemplateService.getSummaries()
      .subscribe(externalTemplates => {
        this.externalProviderTemplateSummaries = externalTemplates;

        for (let i = 0; i < this.externalProviderTemplateSummaries.length; i++) {
          this.externalTemplateMapType[this.externalProviderTemplateSummaries[i].id] = this.externalProviderTemplateSummaries[i]
        }
        this.refreshDataSource();
      })
  }
}
