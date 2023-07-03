import { Component, Inject, Input, OnInit } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { ExternalProviderResponse } from '../../domain/external-provider-response';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ExternalProviderTemplateService } from '../../../../services/external-provider-template.service';
import {
  ExternalProviderTemplateSummaryResponse
} from '../../../../services/domain/external-provider-template-summary-response';
import { CreateApplicationDialog } from '../application-overview/application-overview.component';


@Component({
  selector: 'add-external-provider-dialog',
  templateUrl: 'add-external-provider-dialog.html',
  styleUrls: ['add-external-provider-dialog.css']
})
export class AddExternalProviderDialog {
  addProviderDialogFormGroup: UntypedFormGroup;
  constructor(public dialogRef: MatDialogRef<LoginBoxOverviewComponent>, private _formBuilder: UntypedFormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.addProviderDialogFormGroup = this._formBuilder.group({
      externalProviderTemplateId: ['', Validators.required],
      clientId: ['', Validators.required],
      clientSecret: ['', Validators.required],
      scope: ['', Validators.required],
    });
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

  constructor(private externalProviderTemplateService: ExternalProviderTemplateService, private dialog: MatDialog) {
  }

  onRowClick(index: number) {
    console.log('clicked on row: ', index)
  }


  addProvider() {
    const dialogRef = this.dialog.open(AddExternalProviderDialog, {
      data: {externalProviderTemplates: this.externalProviderTemplateSummaries, tenantId: this.data.tenantId},
    });

    dialogRef.afterClosed().subscribe(result => {

     console.log('add provider dialog result', result)

    });

  }

  updateLogo() {

  }

  ngOnInit(): void {
    this.externalProviderTemplateService.getSummaries()
      .subscribe(externalTemplates => {
        this.externalProviderTemplateSummaries = externalTemplates;

        for (let i = 0; i < this.externalProviderTemplateSummaries.length; i++) {
          this.externalTemplateMapType[this.externalProviderTemplateSummaries[i].id] = this.externalProviderTemplateSummaries[i]
        }
      })
  }
}
