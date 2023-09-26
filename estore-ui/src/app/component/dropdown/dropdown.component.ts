import { Component, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { Router } from '@angular/router';
import { BuyerInfoService } from 'src/app/service/buyerInfo.service';
import { BuyerInfo } from 'src/app/type';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.scss']
})
export class DropdownComponent {
  @ViewChild(MatMenuTrigger) trigger!: MatMenuTrigger;
  
  constructor(private router: Router, private buyerInfoService: BuyerInfoService){}

  openMenu(){
    this.trigger.openMenu();
  }

  redirectToMyAccount() {
    this.buyerInfoService.getBuyerInfoByUser().subscribe((data: BuyerInfo) => {
      if(data != null) {
        this.router.navigateByUrl('/account/' + data.id);
      }
    });
  }
  redirectTo(path: string) {
    this.router.navigateByUrl('/' + path);
  }
  isAdmin(): boolean {
    return localStorage.getItem("token") == 'admin:admin';
  }
}


