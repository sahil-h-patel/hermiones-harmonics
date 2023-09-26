import { Component, Input } from '@angular/core';
import { BuyerInfoService } from '../../service/buyerInfo.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { BuyerInfo } from '../../type';

@Component({
  selector: 'app-buyerInfo-detail',
  templateUrl: './buyerInfo-detail.component.html',
  styleUrls: ['./buyerInfo-detail.component.scss'],
})
export class BuyerInfoDetailComponent {
 

  constructor(
    private route: ActivatedRoute,
    private buyerInfoService: BuyerInfoService,
    private location: Location
  ) {
    const token = localStorage.getItem('token');
    const thisPath = this.location.path()
    const thisID = thisPath.split("/")[2]
    if(token != 'admin:admin') {
      this.buyerInfoService.getBuyerInfoByUser()
        .subscribe((buyerInfo: BuyerInfo) => {
          if(buyerInfo == null || buyerInfo.id.toString() != thisID) {
            this.location.back();
          }
        });
    }
  }

  buyerInfo!: BuyerInfo;
  public isEditingName: boolean = false;
  public isEditingContactDetails: boolean = false;
  public isEditingShippingAddresses: boolean = false;
  public isEditingCreditCards: boolean = false;

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.getBuyerInfo(params['id']);
    });
    console.log(
      'ngonit buyer info: ' + this.buyerInfo + ' with id: ' + this.buyerInfo?.id
    );
  }

  getBuyerInfo(id: number): void {
    this.buyerInfoService
      .getBuyerInfo(id)
      .subscribe((buyerInfo) => (this.buyerInfo = buyerInfo));
  }

  goBack(): void {
    this.location.back();
  }

  updateBuyerInfo() {
    this.buyerInfoService.updateBuyerInfo(this.buyerInfo).subscribe(() => {
      console.log("buyer info updated successfully")
    });
  }

  save(): void {
    if (this.buyerInfo) {
      this.buyerInfoService
        .updateBuyerInfo(this.buyerInfo)
        .subscribe(() => this.goBack());
    }
  }

  appendShippingAddress(shippingAddress: string): void {
    this.buyerInfo.shippingAddresses.push(shippingAddress);
    this.updateBuyerInfo();
  }

  deleteShippingAddress(shippingAddressIndex: number): void {
    this.buyerInfo.shippingAddresses.splice(shippingAddressIndex, 1);
    this.updateBuyerInfo();
  }

  updateShippingAddress(index: number, newShippingAddress: string): void {
    this.buyerInfo.shippingAddresses[index] = newShippingAddress;
    this.updateBuyerInfo();
  }

  appendCreditCard(holderName: string, cardNumber: string): void {
    this.buyerInfo.creditCards.push({ holderName, cardNumber });
    this.updateBuyerInfo();
  }

  deleteCreditCard(creditCardIndex: number): void {
    this.buyerInfo.creditCards.splice(creditCardIndex, 1);
    this.updateBuyerInfo();
  }

  updateCreditCard(index: number, holderName: string, cardNumber: string): void {
    this.buyerInfo.creditCards[index] = {holderName, cardNumber};
    this.updateBuyerInfo();
  }

  getLastFourDigitsCard(index: number): string {
    const cardNumber = this.buyerInfo.creditCards[index].cardNumber;
    return cardNumber.substring(cardNumber.length - 4);
  }

  saveName(name: string): void {
    this.buyerInfo.name = name;
    this.updateBuyerInfo();
    this.isEditingName = false;
  }

  saveContactDetails(phoneNumber: string): void {
    this.buyerInfo.phoneNumber = phoneNumber;
    this.updateBuyerInfo();
    this.isEditingContactDetails = false;
  }

  

}
