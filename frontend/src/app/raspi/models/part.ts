export class Image {
    mediaType: string;
    content: object;
}

export class Part {
    id: number;
    name: string;
    cost: number;
    quantity: number;
    shortDescription: string;
    description: string;
    url: string;
    orderDate: string;
    orderId: string;

    constructor(name: string, cost: number, quantity: number, shortDescription: string, description: string,
                url: string, orderDate: string = '', orderId: string = '') {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.shortDescription = shortDescription;
        this.description = description;
        this.url = url;
        this.orderDate = orderDate;
        this.orderId = orderId;
    }
}
