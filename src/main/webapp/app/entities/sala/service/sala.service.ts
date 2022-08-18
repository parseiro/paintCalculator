import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISala, NewSala } from '../sala.model';

export type PartialUpdateSala = Partial<ISala> & Pick<ISala, 'id'>;

export type EntityResponseType = HttpResponse<ISala>;
export type EntityArrayResponseType = HttpResponse<ISala[]>;

@Injectable({ providedIn: 'root' })
export class SalaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sala: NewSala): Observable<EntityResponseType> {
    return this.http.post<ISala>(this.resourceUrl, sala, { observe: 'response' });
  }

  update(sala: ISala): Observable<EntityResponseType> {
    return this.http.put<ISala>(`${this.resourceUrl}/${this.getSalaIdentifier(sala)}`, sala, { observe: 'response' });
  }

  partialUpdate(sala: PartialUpdateSala): Observable<EntityResponseType> {
    return this.http.patch<ISala>(`${this.resourceUrl}/${this.getSalaIdentifier(sala)}`, sala, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISala>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISala[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSalaIdentifier(sala: Pick<ISala, 'id'>): number {
    return sala.id;
  }

  compareSala(o1: Pick<ISala, 'id'> | null, o2: Pick<ISala, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaIdentifier(o1) === this.getSalaIdentifier(o2) : o1 === o2;
  }

  addSalaToCollectionIfMissing<Type extends Pick<ISala, 'id'>>(
    salaCollection: Type[],
    ...salasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salas: Type[] = salasToCheck.filter(isPresent);
    if (salas.length > 0) {
      const salaCollectionIdentifiers = salaCollection.map(salaItem => this.getSalaIdentifier(salaItem)!);
      const salasToAdd = salas.filter(salaItem => {
        const salaIdentifier = this.getSalaIdentifier(salaItem);
        if (salaCollectionIdentifiers.includes(salaIdentifier)) {
          return false;
        }
        salaCollectionIdentifiers.push(salaIdentifier);
        return true;
      });
      return [...salasToAdd, ...salaCollection];
    }
    return salaCollection;
  }
}
