import { Component, Input } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationSummary } from '../../domain/application-summary';


@Component({
  selector: 'create-role-dialog',
  templateUrl: './create-role-dialog.html',
  styleUrls: ['./create-role-dialog.css']
})
export class CreateRoleDialog {
  fontStyleControl = new UntypedFormControl('');
  fontStyle?: string;
  createRoleFormGroup: UntypedFormGroup;

  // @ts-ignore
  constructor(public dialogRef: MatDialogRef<RolesOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
    this.createRoleFormGroup = this._formBuilder.group({
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
  selector: 'app-roles-overview',
  templateUrl: './roles-overview.component.html',
  styleUrls: ['./roles-overview.component.css']
})
export class RolesOverviewComponent implements DynamicContentViewItem {

  @Input() data: any;
  displayedColumns: string[] = ['name', 'clientId', 'type'];
  dataSource: ApplicationSummary[] = [];
  constructor(private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) { }

  create() {
    const dialogRef = this.dialog.open(CreateRoleDialog, {
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
