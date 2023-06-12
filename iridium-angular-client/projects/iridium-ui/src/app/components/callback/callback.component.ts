import { Component } from '@angular/core';
import { NgxIridiumClientService } from '../../../../../ngx-iridium-client/src/lib/ngx-iridium-client.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent {

  constructor(private router: Router, private iridiumClient: NgxIridiumClientService) {
    console.log('callback constructor')
    this.iridiumClient.authorize()
      .then(successful => {
        if (successful) {
          console.log('auth was successful in callback')
          //this.router.navigateByUrl("/dashboard")

        } else {
          console.log('auth was not successful')
        }

      });
  }

}
