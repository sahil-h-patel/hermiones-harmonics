export interface Product {
  id: number;
  name: string;
  tags: string[];
  description: string;
  price: number;
  quantity: number;
  image?: string;
}

export interface BuyerInfo {
  id: number;
  userId: number;
  name: string;
  phoneNumber: string;
  pastOrdersIds: Array<number>;
  creditCards: Array<CreditCard>;
  shippingAddresses: Array<string>;
  cart: Array<number>;
  wishlist: Array<number>;
}

export interface CreditCard {
  holderName: string;
  cardNumber: string;
}

export interface Cart {
  cart: Array<Product>;
}

export interface Order {
  productId: number[];
  date: number;
  orderStatus: keyof typeof OrderStatus;
  ccDigits: number;
  address: string;
  orderID: number;
  userID: number;
}

export enum OrderStatus {
  UNPROCESSED,
  SHIPPED,
  DELIVERED,
  CANCELLED,
}
