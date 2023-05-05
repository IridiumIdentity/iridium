import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  imagePath: string;

  constructor() {
    this.imagePath = '/assets/iridium-3C-xl.png';
  }

}
