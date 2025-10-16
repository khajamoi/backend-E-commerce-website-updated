package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Address;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.AddressRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a new address for a specific user.
     */
    public Address addAddress(Long userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Set this address to the user
        address.setUser(user);

        // If it's marked as default, make other addresses non-default
        if (address.isDefault()) {
            List<Address> existingAddresses = addressRepository.findByUser(user);
            for (Address addr : existingAddresses) {
                if (addr.isDefault()) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            }
        }

        return addressRepository.save(address);
    }

    /**
     * Get all addresses for a given user.
     */
    public List<Address> getAddressesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return addressRepository.findByUser(user);
    }

    /**
     * Update an existing address.
     */
    public Address updateAddress(Long addressId, Address updatedAddress) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressId));

        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setLandmark(updatedAddress.getLandmark());
        existingAddress.setPhoneNumber(updatedAddress.getPhoneNumber());
        existingAddress.setAddressType(updatedAddress.getAddressType());
        existingAddress.setDefault(updatedAddress.isDefault());

        return addressRepository.save(existingAddress);
    }

    /**
     * Delete an address by ID.
     */
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new EntityNotFoundException("Address not found with ID: " + addressId);
        }
        addressRepository.deleteById(addressId);
    }

    /**
     * Get default address of a user.
     */
    public Address getDefaultAddress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return addressRepository.findByUserAndIsDefaultTrue(user);
    }
}
