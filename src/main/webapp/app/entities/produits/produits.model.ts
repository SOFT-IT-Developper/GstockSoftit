import { OutStock } from '../out-stock';
import { Stock } from '../stock';
import { Categorie } from '../categorie';
export class Produits {
    constructor(
        public id?: number,
        public name?: string,
        public emplacement?: string,
        public description?: string,
        public captureContentType?: string,
        public capture?: any,
        public stock?: Stock,
        public categorie?: Categorie,
    ) {
    }
}
