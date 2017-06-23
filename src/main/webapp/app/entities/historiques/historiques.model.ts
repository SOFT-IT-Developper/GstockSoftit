import { User } from '../../shared';
export class Historiques {
    constructor(
        public id?: number,
        public operation?: string,
        public date?: any,
        public user?: User,
    ) {
    }
}
