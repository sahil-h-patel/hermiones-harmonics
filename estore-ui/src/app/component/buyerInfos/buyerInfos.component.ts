import { Component } from '@angular/core';
import { BuyerInfoService } from '../../service/buyerInfo.service';
import { BuyerInfo } from '../../type';
import { Location } from '@angular/common';

@Component({
  selector: 'app-buyerInfos',
  templateUrl: './buyerInfos.component.html',
  styleUrls: ['./buyerInfos.component.scss'],
})
export class BuyerInfosComponent {
  buyerInfos: BuyerInfo[] = [];

  constructor(private buyerInfoService: BuyerInfoService, private location: Location) {
    const token = localStorage.getItem('token');
    if (token == null || token !== 'admin:admin') {
      this.location.back();
    }
    
    buyerInfoService.getBuyerInfos().subscribe((data) => {
      this.buyerInfos = data ? data : [];
    });
  }

  setBuyerInfosList(buyerInfos: BuyerInfo[]): void {
    if (!buyerInfos) {
      this.buyerInfos = [];
      return console.error(
        'BuyerInfosComponent.setBuyerInfos(): buyerInfos is null'
      );
    }
    this.buyerInfos = buyerInfos;
  }

  getBuyerInfos() {
    this.buyerInfoService.getBuyerInfos().subscribe(this.setBuyerInfosList);
  }

  addBuyerInfo(buyerInfo: BuyerInfo) {
    const id: number = 0; // Server will handle the actual buyerInfo ID, replacing this temporary value.
    this.buyerInfoService
      .addBuyerInfo(buyerInfo)
      .subscribe((buyerInfo: BuyerInfo) => {
        this.buyerInfos.push(buyerInfo);
        // this.getBuyerInfos(); // Refreshes buyerInfo list
        console.log('BuyerInfo created successfully');
      });
  }

  addNewBuyerInfo(
    userId: number,
    name: string,
    phoneNumber: string
  ) {
    const newBuyerInfo: BuyerInfo = {
      id: 0, // temporary value
      userId,
      name,
      phoneNumber,
      pastOrdersIds: [],
      creditCards: [],
      shippingAddresses: [],
      cart: [],
      wishlist: [],
    };

    this.addBuyerInfo(newBuyerInfo);
  }

  updateBuyerInfo(buyerInfo: BuyerInfo) {
    this.buyerInfoService.updateBuyerInfo(buyerInfo).subscribe((buyerInfo) => {
      this.buyerInfos = this.buyerInfos.map((b) =>
        b.id === buyerInfo.id ? buyerInfo : b
      );
      // this.getBuyerInfos();
      console.log('BuyerInfo updated successfully');
    });
  }

  deleteBuyerInfo(id: number) {
    this.buyerInfoService.deleteBuyerInfo(id).subscribe((buyerInfo) => {
      this.buyerInfos = this.buyerInfos.filter((b) => b.id !== buyerInfo.id);
      // this.getBuyerInfos();
      console.log('BuyerInfo deleted successfully');
    });
  }
}
