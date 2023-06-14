import { Component, Inject, OnInit } from '@angular/core';
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

  hasError!: boolean;
  iridiumImagePath: string;
  environment: any;
  constructor(
    private router: Router,
    @Inject('environment') environment:any,
    private iridiumClient: NgxIridiumClientService
  ) {
    this.iridiumImagePath = '/assets/iridium-3C-xl.png';

    console.log('in constructor env is ', environment)
    this.environment = environment;
  }

  ngOnInit(): void {
    this.iridiumClient.authorize()
      .then((successful) => {
        if (successful) {
          this.hasError = false;
        }
        if (this.environment.iridium.successfulAuthDestination !== null && this.environment.iridium.successfulAuthDestination !== undefined) {
          this.router.navigateByUrl(this.environment.iridium.successfulAuthDestination)
        }
      }

      ).catch((error) => {
        console.error("error! ", error)
        this.hasError = true;
      }
    )
  }

}
