import { Component, Input, OnInit, } from '@angular/core';
import { DynamicContentViewItem } from '../dynamic-content-view-item';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CookieService } from '../../../../services/cookie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../../../services/identity.service';
import { IdentitySummaryResponse } from '../../domain/identity-summary-response';

// @Component({
//   selector: 'add-user-dialog',
//   templateUrl: './add-user-dialog.html',
//   styleUrls: ['./add-user-dialog.css']
// })
// export class AddUserDialog {
//   createUserFormGroup: UntypedFormGroup;
//
//   constructor(public dialogRef: MatDialogRef<UserOverviewComponent>, private _formBuilder: UntypedFormBuilder) {
//     this.createUserFormGroup = this._formBuilder.group({
//       username: ['', Validators.required],
//       password: ['', Validators.required],
//     });
//   }
//
//   create() {
//     console.log('yes')
//   }
// }
type IdentitySummaryMapType = {
  [id: string]: IdentitySummaryResponse;
}
@Component({
  selector: 'app-user-overview',
  templateUrl: './user-overview.component.html',
  styleUrls: ['./user-overview.component.css']
})
export class UserOverviewComponent implements DynamicContentViewItem, OnInit {
  @Input() data: any;
  displayedColumns: string[] = ['id', 'emailAddress'];
  dataSource: IdentitySummaryResponse[] = [];

  constructor(private identityServie: IdentityService, private cookieService: CookieService, private route: ActivatedRoute, private _formBuilder: UntypedFormBuilder, private router: Router, private dialog: MatDialog) { }


  // create() {
  //   const dialogRef = this.dialog.open(AddUserDialog, {
  //     data: {},
  //   });
  //
  //   dialogRef.afterClosed().subscribe(result => {
  //     console.log('The dialog was closed');
  //   });
  // }
  onRowClick(index: number) {
    console.log('clicked on row: ', index)
  }

  ngOnInit(): void {
    this.refreshDataSource();

  }

  private refreshDataSource() {
    this.dataSource = [];
    this.identityServie.getSummariesByParentTenant(this.data.tenantId, 100)
      .subscribe(identitySummaries => {
        console.log('identity summaries is ', identitySummaries)
        for (let i = 0; i < identitySummaries.data.length; i++) {
          let summary = identitySummaries.data[i];
          console.log('summary is', summary)
          const newRow = {
            id: summary.id,
            emailAddress: summary.emailAddress,
          };
          console.log('adding new row', newRow)
          this.dataSource = [...this.dataSource, newRow]
        }

      })
  }
}
