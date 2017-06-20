import { Produits } from '../produits';
export class Stock {
    constructor(
        public id?: number,
        public quantite?: number,
        public description?: string,
        public date?: any,
        public produit?: Produits,
    ) {
    }
}
