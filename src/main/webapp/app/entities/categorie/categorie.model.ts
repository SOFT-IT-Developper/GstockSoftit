import { Produits } from '../produits';
export class Categorie {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public produit?: Produits,
    ) {
    }
}
