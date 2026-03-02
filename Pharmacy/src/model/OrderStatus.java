package model;

/**
 * Starile unei comenzi: PENDING este cel implicit, care astepta aprobarea managerului (APPROVED/DENIED), iar comenzile aprobate pot fi dupa declarate
 * ca si completate pentru a actualiza stocul de produse.
 */
public enum OrderStatus {
	PENDING,
    APPROVED,
    DENIED,
    COMPLETED
}
