import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxIridiumClientService } from 'ngx-iridium-client';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  loggedIn = false;

  constructor() {

  }

  ngOnInit(): void {

  }


}

