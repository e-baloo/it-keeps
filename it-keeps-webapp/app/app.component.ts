import {Component, EventEmitter} from '@angular/core';
import {AuthService} from "./service/auth.service";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'my-app',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.css']
})

export class AppComponent {

  private loggedInEmitter : EventEmitter<boolean> = null;
  private roleEmitter : EventEmitter<string> = null;
  
  
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {
    
    this.loggedInEmitter = this.authService.getLoggedInEmitter();
    this.roleEmitter = this.authService.getRoleEmitter();
  
    this.roleEmitter.subscribe(
      (role:string) => console.log("Role = " + role)
    )
    
    
  }
  
  logout(): void {
    
    this.authService.logout();
    this.router.navigate(['/welcome']);
  
  }
  
}

