import { Produits } from '../produits';
export class OutStock {
    constructor(
        public id?: number,
        public quantite?: number,
        public date?: any,
        public cause?: string,
        public produit?: Produits,
    ) {
    }
}
