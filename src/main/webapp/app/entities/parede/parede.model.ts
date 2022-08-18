import { ISala } from 'app/entities/sala/sala.model';

export interface IParede {
  id: number;
  largura?: number | null;
  altura?: number | null;
  area?: number | null;
  numPortas?: number | null;
  numJanelas?: number | null;
  sala?: Pick<ISala, 'id' | 'nome'> | null;
}

export type NewParede = Omit<IParede, 'id'> & { id: null };
