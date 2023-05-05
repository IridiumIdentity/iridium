import { Component, Input } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { FrontEndClientSummary } from '../../domain/frontEndClientSummary';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';



@Component({
  selector: 'create-api-dialog',
  templateUrl: './create-api-dialog.html',
  styleUrls: ['./create-api-dialog.css']
})
export class CreateAPIDialog {
  fontStyleControl = new UntypedFormControl('');
  fontStyle?: string;
  createApiFormGroup: UntypedFormGroup;

  // @ts-ignore
  constructor(public dialogRef: MatDialogRef<ApiOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
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
  selector: 'app-api-overview',
  templateUrl: './api-overview.component.html',
  styleUrls: ['./api-overview.component.css']
})
export class ApiOverviewComponent implements DynamicContentViewItem {
  @Input() data: any;


  displayedColumns: string[] = ['name', 'clientId', 'type'];
  dataSource: FrontEndClientSummary[] = [];
  createApiFormGroup: UntypedFormGroup;

  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) {
    this.createApiFormGroup = this._formBuilder.group({
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
    const dialogRef = this.dialog.open(CreateAPIDialog, {
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
