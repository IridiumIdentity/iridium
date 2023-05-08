import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxIridiumClientService } from 'ngx-iridium-client';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

interface TenantSelectItem {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  loggedIn = false;

  constructor(private router: Router, private iridiumClient: NgxIridiumClientService) {

  }

  ngOnInit(): void {
        console.log('check auth status')
    }
  title = 'Iridium UI';

  login() {
    console.log('authenticate with ext')
    this.iridiumClient.authenticateWithExternalRedirect();
    //this.router.navigateByUrl('/login');

  }

  register() {
    this.router.navigateByUrl('/register');
  }

  homeButtonClick(): void {
    this.router.navigateByUrl('/');
 }
}

