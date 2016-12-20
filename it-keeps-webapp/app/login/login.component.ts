import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';


import {AuthService} from "../service/auth.service";
import {jCredential} from "../model/jCredential";
import {AlertService} from "../service/alert.service";

//<script src="/node_modules/bootstrap-show-password/bootstrap-show-password.js"></script>

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;
    returnUrl: string;
    authTypes: string[] = null;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authService: AuthService,
        private alertService: AlertService) { }

    ngOnInit() {
        // reset login status
        this.authService.logout();
        this.authService.getAuthTypeEnum().subscribe(data => this.authTypes = data);
        this.model.type = 'BASIC';


      // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    }

    login() {
        this.loading = true;

        let cred: jCredential = new jCredential;
        cred.setName(this.model.username);
        cred.setPassword(this.model.password);
        cred.setAuthenticationType(this.model.type);

        this.authService.login(cred).subscribe(
          () => {
            this.router.navigate([this.returnUrl]);
          },
          error => {
            this.alertService.error(error);
            this.loading = false;
          });

    }
}
