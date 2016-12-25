import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {PathService} from "../service/path.service";
import {EntryService} from "../service/entry.service";


@Component({
    moduleId: module.id,
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  userName: string = '';
  userId: string = '';

    constructor(
      private authService: AuthService,
      private pathService : PathService,
      private entryService : EntryService
    ) {
        this.userName = this.authService.getUserName();
        this.userId = this.authService.getUserId()
    }

    ngOnInit() {
      
      this.pathService.getAll().subscribe(
        paths => console.log(paths),
        error => console.error(error),
        () => console.log("done")
      );
  
      this.entryService.getAll().subscribe(
        paths => console.log(paths),
        error => console.error(error),
        () => console.log("done")
      );
  
  
    }

}
