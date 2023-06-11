import { Component, OnInit } from '@angular/core';
import { AccessTokenResponse } from './domain/access-token-response';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthorizationService } from './service/authorization.service';
import { NgxIridiumClientService } from './ngx-iridium-client.service';

@Component({
  selector: 'lib-ngx-iridium-client',
  templateUrl: './ngx-iridium-callback.component.html',
  styleUrls: ['./ngx-iridium-callback.component.css']
})
export class NgxIridiumCallbackComponent implements OnInit {

  hasError: boolean;
  iridiumImagePath: string;

  constructor(
    private router: Router,

    private iridiumClient: NgxIridiumClientService
  ) {
    this.iridiumImagePath = '/assets/iridium-3C-xl.png';
    this.hasError = false;
  }

  ngOnInit(): void {
    this.iridiumClient.authorize()
      .then((successful) => {
        console.log('is successful: ' + successful)
        this.router.navigateByUrl("/")
        }
      ).catch((error) => {
        console.error("error! ", error)
      }
    )
  }

}
