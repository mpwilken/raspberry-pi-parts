import {$, browser, ElementArrayFinder, ElementFinder} from 'protractor';

class PartListHeaderRow {
  constructor(public element: ElementFinder) {
  }

  private getHeaderCell(index: number): ElementFinder {
    return this.element.$$('th').get(index);
  }

  getNameCell() {
    return this.getHeaderCell(0);
  }

  getCostCell() {
    return this.getHeaderCell(1);
  }

  getQuantityCell() {
    return this.getHeaderCell(2);
  }

  getDescriptionCell() {
    return this.getHeaderCell(3);
  }
}

class PartListRow {
  constructor(public element: ElementFinder) {
  }

  private getCell(index: number) {
    return this.element.$$('th,td').get(index);
  }

  getName(): ElementFinder {
    return this.getCell(0);
  }

  getCost() {
    return this.getCell(1);
  }

  getQuantity() {
    return this.getCell(2);
  }

  getDescription() {
    return this.getCell(3);
  }
}

class PartsList {
  constructor(public element: ElementFinder) {
  }

  getRows(): ElementArrayFinder {
    return this.element.$$('.part-list__table-body tr');
  }

  getRow(index: number): PartListRow {
    return new PartListRow(this.getRows().get(index));
  }

}

export class PartListPage {
  navigateTo(destination = '/') {
    return browser.get(destination);
  }

  getPartsList() {
    return new PartsList($('app-part-list'));
  }
}
