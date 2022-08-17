export interface ISala {
  id: number;
  nome?: string | null;
}

export type NewSala = Omit<ISala, 'id'> & { id: null };
