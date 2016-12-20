import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";


@Component({
    moduleId: module.id,
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  userName: string = '';
  userId: string = '';

    constructor(private authService: AuthService) {
        this.userName = this.authService.getUserName();
        this.userId = this.authService.getUserId()
    }

    ngOnInit() {
    }

}
